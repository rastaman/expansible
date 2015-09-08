import {Component, View, bootstrap} from 'angular2/angular2';
import {Frontend} from 'frontend';

@Component({
  selector: 'main'
})

@View({
  directives: [Frontend],
  template: `
    <frontend></frontend>
  `
})

class Main {

}

bootstrap(Main);
