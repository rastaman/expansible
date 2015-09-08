package io.expansible.core;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import io.expansible.ExpansibleConfiguration;
import io.expansible.model.Inventory;
import io.expansible.resources.InventoriesApi.IInventoriesApiService;

public class InventoriesApiService implements IInventoriesApiService {

  private ExpansibleConfiguration configuration;
  
  private List<String> hostsPaths = new ArrayList<String>();

  public InventoriesApiService() {
    //
  }

  public InventoriesApiService(ExpansibleConfiguration configuration) {
    this.configuration = configuration;
    init();
  }

  private void init() {
    this.hostsPaths.clear();
    if ( this.configuration != null ) {
      this.hostsPaths.add( this.configuration.bootstrapHosts );
    }
  }

  @Override
  public List<Inventory> inventoriesGet(String type) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Inventory create(Inventory i) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Inventory> readAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Optional<Inventory> readById(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

}
