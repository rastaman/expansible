import {Component, View} from 'angular2/angular2';

@Component({
  selector: 'frontend'
})

@View({
  templateUrl: 'frontend.html'
})

export class Frontend {

  constructor() {
    console.info('Frontend Component Mounted Successfully');
  }

}
