import {environment} from "../../environments/environment";

export const API_URL = environment.BASE_URL
export const GET_CLIENT_DATA = API_URL + 'client/'
export const GET_INSTRUMENT_DATA = API_URL + 'instrument/'
export const POST_TRANSACTION_DATA = API_URL + 'transaction/'
