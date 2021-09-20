import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GET_ALL_ORDER_BOOK, GET_CLIENT_DATA, GET_INSTRUMENT_DATA, POST_TRANSACTION_DATA} from "../models/constant";
import {Client, Instrument, OrderBook} from "../models/models";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(
    private http: HttpClient
  ) {
  }

  getClientData(value: any): Observable<Client> {
    return this.http.get<Client>(GET_CLIENT_DATA + value);
  }

  getInstrument(value: any): Observable<Instrument> {
    return this.http.get<Instrument>(GET_INSTRUMENT_DATA + value);
  }

  postTransaction(value: any): Observable<OrderBook> {
    return this.http.post<OrderBook>(POST_TRANSACTION_DATA, value);
  }
  getAllOrderBook(){
    return this.http.get<OrderBook[]>(GET_ALL_ORDER_BOOK);
  }
}
