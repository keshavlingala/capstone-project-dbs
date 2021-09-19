import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError, tap} from 'rxjs/operators';
import { Client } from "../models/models";
@Injectable({
  providedIn: 'root'
})
export class DataService {

  url="http://localhost:8000";
  constructor(private http:HttpClient) { }

  getClientById(clientId: string): Observable<Client> {
    return this.http.get<Client>(this.url + "/" + clientId).pipe(
        tap(client => console.log(client.clientName + " " + client.clientId)),
        catchError(this.handleError)
    );
}

private handleError(error: any) {
  console.error(error);
  return throwError(error);
}

}
