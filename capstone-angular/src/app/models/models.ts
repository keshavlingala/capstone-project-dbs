export interface Client {
  clientId: string;
  clientName: string;
  custodian: Custodian;
  transactionLimit: number
}

export interface Custodian {
  custodianId: string;
  custodianName: string;
}

export interface Instrument {
  instrumentId: string;
  instrumentName: string;
  faceValue?: number;
  expiryDate?: Date;
  minQuantity: number;
}

export interface OrderBook {
  orderId: string;
  client: Client,
  "instrument": Instrument,
  "price": number
  "quantity": number;
  initialQuantity: number;
  "orderStatus": string;
  "orderDirection": string;
  "limitOrder": boolean;
  "timeStamp": Date
}

export interface CustodianStat {
  "custodianId": string;
  "totalBuy": number;
  "totalSell": number;
}

export interface ClientStat {
  "clientId": string;
  "totalBuy": number;
  "totalSell": number;
}
