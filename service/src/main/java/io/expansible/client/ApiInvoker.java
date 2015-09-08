package io.expansible.client;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.TimeZone;

import java.net.URLEncoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.text.SimpleDateFormat;
import java.text.ParseException;

public class ApiInvoker {
  private static ApiInvoker INSTANCE = new ApiInvoker();
  private Map<String, Client> hostMap = new HashMap<String, Client>();
  private Map<String, String> defaultHeaderMap = new HashMap<String, String>();
  private boolean isDebug = false;

  /**
   * ISO 8601 date time format.
   * @see "https://en.wikipedia.org/wiki/ISO_8601"
   */
  public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

  /**
   * ISO 8601 date format.
   * @see "https://en.wikipedia.org/wiki/ISO_8601"
   */
  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  static {
    // Use UTC as the default time zone.
    DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));

    // Set default User-Agent.
    setUserAgent("Java-Swagger");
  }

  public static void setUserAgent(String userAgent) {
    INSTANCE.addDefaultHeader("User-Agent", userAgent);
  }

  public static Date parseDateTime(String str) {
    try {
      return DATE_TIME_FORMAT.parse(str);
    } catch (java.text.ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static Date parseDate(String str) {
    try {
      return DATE_FORMAT.parse(str);
    } catch (java.text.ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static String formatDateTime(Date datetime) {
    return DATE_TIME_FORMAT.format(datetime);
  }

  public static String formatDate(Date date) {
    return DATE_FORMAT.format(date);
  }

  public static String parameterToString(Object param) {
    if (param == null) {
      return "";
    } else if (param instanceof Date) {
      return formatDateTime((Date) param);
    } else if (param instanceof Collection) {
      StringBuilder b = new StringBuilder();
      for(Object o : (Collection)param) {
        if(b.length() > 0) {
          b.append(",");
        }
        b.append(String.valueOf(o));
      }
      return b.toString();
    } else {
      return String.valueOf(param);
    }
  }
  public void enableDebug() {
    isDebug = true;
  }

  public static ApiInvoker getInstance() {
    return INSTANCE;
  }

  public void addDefaultHeader(String key, String value) {
     defaultHeaderMap.put(key, value);
  }

  public String escapeString(String str) {
    try{
      return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
    }
    catch(UnsupportedEncodingException e) {
      return str;
    }
  }

  public static Object deserialize(String json, String containerType, Class cls) throws ApiException {
    if(null != containerType) {
        containerType = containerType.toLowerCase();
    }
    try{
      if("list".equals(containerType) || "array".equals(containerType)) {
        JavaType typeInfo = JsonUtil.getJsonMapper().getTypeFactory().constructCollectionType(List.class, cls);
        List response = (List<?>) JsonUtil.getJsonMapper().readValue(json, typeInfo);
        return response;
      }
      else if(String.class.equals(cls)) {
        if(json != null && json.startsWith("\"") && json.endsWith("\"") && json.length() > 1)
          return json.substring(1, json.length() - 2);
        else
          return json;
      }
      else {
        return JsonUtil.getJsonMapper().readValue(json, cls);
      }
    }
    catch (IOException e) {
      throw new ApiException(500, e.getMessage());
    }
  }

  public static String serialize(Object obj) throws ApiException {
    try {
      if (obj != null)
        return JsonUtil.getJsonMapper().writeValueAsString(obj);
      else
        return null;
    }
    catch (Exception e) {
      throw new ApiException(500, e.getMessage());
    }
  }

  public String invokeAPI(String host, String path, String method, Map<String, String> queryParams, Object body, Map<String, String> headerParams, Map<String, String> formParams, String contentType) throws ApiException {
    Client client = getClient(host);

    StringBuilder b = new StringBuilder();

    for(String key : queryParams.keySet()) {
      String value = queryParams.get(key);
      if (value != null){
        if(b.toString().length() == 0)
          b.append("?");
        else
          b.append("&");
        b.append(escapeString(key)).append("=").append(escapeString(value));
      }
    }
    String querystring = b.toString();

    Builder builder = client.target(host + path + querystring).request().accept("application/json");
    for(String key : headerParams.keySet()) {
      builder = builder.header(key, headerParams.get(key));
    }

    for(String key : defaultHeaderMap.keySet()) {
      if(!headerParams.containsKey(key)) {
        builder = builder.header(key, defaultHeaderMap.get(key));
      }
    }
    Response response = null;

    if("GET".equals(method)) {
      response = builder.get(Response.class);
    }
    else if ("POST".equals(method)) {
      if(body == null)
        response = builder.post(Entity.text((String)null));
      else if(body instanceof FormDataMultiPart) {
        response = builder.header("Content-Type",contentType).post(Entity.text(body.toString()));
      }
      else
        response = builder.header("Content-Type",contentType).post(Entity.text(serialize(body)));
    }
    else if ("PUT".equals(method)) {
      if(body == null)
        response = builder.put(Entity.text((String)null));
      else {
        if("application/x-www-form-urlencoded".equals(contentType)) {
          StringBuilder formParamBuilder = new StringBuilder();

          // encode the form params
          for(String key : formParams.keySet()) {
            String value = formParams.get(key);
            if(value != null && !"".equals(value.trim())) {
              if(formParamBuilder.length() > 0) {
                formParamBuilder.append("&");
              }
              try {
                formParamBuilder.append(URLEncoder.encode(key, "utf8")).append("=").append(URLEncoder.encode(value, "utf8"));
              }
              catch (Exception e) {
                // move on to next
              }
            }
          }
          response = builder.header("Content-Type",contentType).put(Entity.text(formParamBuilder.toString()));
        }
        else
          response = builder.header("Content-Type",contentType).put(Entity.text(serialize(body)));
      }
    }
    else if ("DELETE".equals(method)) {
      if(body == null)
        response = builder.delete();
      else
        response = builder.header("Content-Type",contentType).delete(Response.class);
    }
    else {
      throw new ApiException(500, "unknown method type " + method);
    }
    if(response.getStatusInfo() == Response.Status.NO_CONTENT ) {
      return null;
    }
    else if(response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL ) {
      if(response.hasEntity()) {
        return (String) response.readEntity(String.class);
      }
      else {
        return "";
      }
    }
    else {
      String message = "error";
      if(response.hasEntity()) {
        try{
          message = String.valueOf(response.readEntity(String.class));
        }
        catch (RuntimeException e) {
          // e.printStackTrace();
        }
      }
      throw new ApiException(
                response.getStatus(),
                message);
    }
  }

  private Client getClient(String host) {
    if(!hostMap.containsKey(host)) {
      Client client = ClientBuilder.newClient();
      if(isDebug)
        client.register(new LoggingFilter());
      hostMap.put(host, client);
    }
    return hostMap.get(host);
  }
}
