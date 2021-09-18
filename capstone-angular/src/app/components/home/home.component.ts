import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  data: any;

  constructor(
    private http: HttpClient
  ) {
  }

  async ngOnInit() {
    this.http.get('http://localhost:8000/client/DBS001').subscribe(d => this.data = d);
  }

}
