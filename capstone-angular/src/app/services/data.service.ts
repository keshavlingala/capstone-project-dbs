import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {
  CLIENT_WISE_DATA,
  CUSTODIAN_WISE_DATA,
  GET_ALL_CLIENTS,
  GET_ALL_CUSTODIANS,
  GET_ALL_INSTRUMENT,
  GET_ALL_ORDER_BOOK,
  GET_CLIENT_BY_CUSTODIAN,
  GET_CLIENT_DATA,
  GET_INSTRUMENT_DATA,
  POST_TRANSACTION_DATA
} from "../models/constant";
import {Client, ClientStat, Custodian, CustodianStat, Instrument, OrderBook} from "../models/models";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

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

  getAllOrderBook() {
    return this.http.get<OrderBook[]>(GET_ALL_ORDER_BOOK);
  }

  getRecentHistory() {
    return this.http.get<OrderBook[]>(GET_ALL_ORDER_BOOK).pipe(
      map(orderbook => {
        console.log(orderbook.map(o => new Date(o.timeStamp).getTime()))
        return orderbook.sort((a, b) => new Date(b.timeStamp).getTime() - new Date(a.timeStamp).getTime()).slice(0, 5)
      })
    )
  }

  getCustodianStats() {
    return this.http.get<CustodianStat[]>(CUSTODIAN_WISE_DATA);
  }

  getClientStats() {
    return this.http.get<ClientStat[]>(CLIENT_WISE_DATA);
  }

  getAllInstruments() {
    return this.http.get<Instrument[]>(GET_ALL_INSTRUMENT);
  }

  getAllCustodians() {
    return this.http.get<Custodian[]>(GET_ALL_CUSTODIANS);
  }

  getAllClients() {
    return this.http.get<Client[]>(GET_ALL_CLIENTS);
  }

  getClientsByCustodian(cid: string) {
    return this.http.get<Client[]>(GET_CLIENT_BY_CUSTODIAN + cid)
  }
}
