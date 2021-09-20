import {environment} from "../../environments/environment";

export const API_URL = environment.BASE_URL
export const GET_CLIENT_DATA = API_URL + 'client/'
export const GET_INSTRUMENT_DATA = API_URL + 'instrument/'
export const POST_TRANSACTION_DATA = API_URL + 'transaction/'
export const CUSTODIAN_WISE_DATA = API_URL + 'custodianWiseStats/'
export const CLIENT_WISE_DATA = API_URL + 'clientWiseStats/'
export const GET_ALL_ORDER_BOOK = API_URL + 'orderBook/'
export const GET_ALL_INSTRUMENT = API_URL + 'instruments/'
export const GET_ALL_CLIENTS = API_URL + 'clients/'
export const GET_ALL_CUSTODIANS = API_URL + 'custodians/'
export const GET_CLIENT_BY_CUSTODIAN = API_URL + 'clientsByCustodian/'
