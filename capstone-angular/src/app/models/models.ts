import {HttpErrorResponse} from "@angular/common/http";


export interface Custodian{
  custodianId:string;
  custodianName:string;
}

export interface Instrument{
  instrumentId:string;
  instrumentName:string;
  faceValue:number;
  expiryDate:Date;
  minQuantity:number;
}

export interface Client {
  clientId: string;
  clientName: string;
  transactionLimit: number;
  custodian:{
    custodianId:string;
    custodianName:string;
  }
}

export interface OrderBook{

  orderId:string;
  client:{
    clientId: string;
  clientName: string;
  transactionLimit: number;
  custodian:{
    custodianId:string;
    custodianName:string;
  }},
  instrument:{
    instrumentId:string;
  instrumentName:string;
  faceValue:number;
  expiryDate:Date;
  minQuantity:number;
  },
  price:number;
  quantity:number;
  orderStatus:' PROCESSING'| 'COMPLETED'| 'CANCELLED';
  orderDirection:'BUY' | 'SELL';
  limitOrder:boolean;
  timeStamp:Date;
}
 
export interface Stocks{

  stockId:string;
  client:{
    clientId: string;
  clientName: string;
  transactionLimit: number;
  custodian:{
    custodianId:string;
    custodianName:string;
  }
  },
  instrument:{
    instrumentId:string;
    instrumentName:string;
    faceValue:number;
    expiryDate:Date;
    minQuantity:number;
  },
  quantity:number;

}

// export interface Customer {
//   accountNumber: string;
//   clearBalance: number;
//   name: string;
//   overdraft: boolean;
// }

// export interface Bank {
//   bic: string;
//   name: string;
// }

// export interface TransferTypeCode {
//   value: string;
//   name: string;
// }

// export interface MessageType {
//   messageCode: string;
//   description: string;
// }

// export interface CurrencyType {
//   code: string;
//   name: string;
//   value: number;
//   symbol: string;
// }

// export interface ErrorResponse extends HttpErrorResponse {
//   error: {
//     message: string;
//     description: string;
//   }
// }

// export interface TransactionRequest {
//   payload: {
//     customerId: string;
//     amount: number;
//     messageCode: string;
//     receiverAccountNumber: string;
//     receiverAccountName: string;
//     receiverBIC: string;
//     transferTypeCode: 'C' | 'O'
//   }
// }

// export interface TransactionResponse {
//   sender: {
//     accountNumber: string;
//     name: string;
//     clearBalance: number;
//     overdraft: boolean
//   },
//   transaction: {
//     transferFee: number;
//     totalAmount: number;
//     transferTypeCode: 'C' | 'O'
//   },
//   date: Date;
//   receiverBIC: string;
//   receiverAccountNumber: string;
//   receiverAccountName: string;
//   messageCode: string;
// }

// export interface TransactionItem {
//   transactionID: number,
//   customer: {
//     accountNumber: string
//     name: string
//     clearBalance: number;
//     overdraft: boolean
//   },
//   receiverName: string
//   receiverAccountNumber: string
//   receiverBIC: {
//     bic: string
//     name: string
//   },
//   messageCode: {
//     messageCode: string
//     description: string
//   },
//   transferCode: 'C' | 'O'
//   amount: number
//   timestamp: Date,
//   status: 'SUCCESS' | 'FAILED';
//   failureReason?: string;
// }

// export type MessageCount = [messageCode: string, numberOfTransactions: number];
// export type TopCustomers = [accountNumber: string, remittance: number]
// export type AmountByBank = [name: string, amount: number]
