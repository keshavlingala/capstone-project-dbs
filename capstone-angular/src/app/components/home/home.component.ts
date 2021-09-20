import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {DataService} from "../../services/data.service";
import {priceValidator, quantityValidator} from "./custom.validators";
import {MatDialog} from "@angular/material/dialog";
import {ErrorComponent} from "../error/error.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Observable} from "rxjs";
import {OrderBook} from "../../models/models";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  orderForm: FormGroup
  history!: Observable<OrderBook[]>
  @ViewChild('form') formElement!: NgForm

  constructor(
    private fb: FormBuilder,
    private data: DataService,
    private dialog: MatDialog,
    private snack: MatSnackBar
  ) {
    this.orderForm = fb.group({
      clientId: ['', Validators.required],
      clientName: [{value: '', disabled: true}],
      instrumentId: ['', Validators.required],
      instrumentName: [{value: '', disabled: true}],
      faceValue: [{value: '', disabled: true}],
      expireDate: [{value: '', disabled: true}],
      price: ['', Validators.required],
      quantity: ['', [Validators.required]],
      orderDirection: ['', Validators.required],
    })
    this.history = this.data.getAllOrderBook();
  }

  ngOnInit() {
    this.orderForm.reset()
  }

  makeTransaction() {
    console.log(this.orderForm.value)
    this.data.postTransaction(this.orderForm.value).subscribe(orderBook => {
      console.log(orderBook);
      this.orderForm.reset();
      this.formElement.reset();
      this.formElement.resetForm();
      this.orderForm.markAsPristine();
      this.orderForm.markAsUntouched();
      this.orderForm.updateValueAndValidity();
      this.snack.open('Order Request Successful', 'Dismiss', {
        duration: 1500
      })
    }, error => {
      this.handleError(error);
      this.snack.open('Order Request Failed', 'Try Again', {
        duration: 1500
      })
    })
  }

  updateClient(event: any) {
    console.log(event)
    this.data.getClientData(this.orderForm.get('clientId')?.value).subscribe(client => {
      this.orderForm.get('clientName')?.setValue(client.clientName);
      this.orderForm.get('clientId')?.setErrors(null)
    }, error => {
      this.handleError(error);
      this.orderForm.get('clientName')?.setValue('')
      this.orderForm.get('clientId')?.setErrors({
        invalid: true
      })
    })
  }

  updateInstrument() {
    this.data.getInstrument(this.orderForm.get('instrumentId')?.value).subscribe(instrument => {
      this.orderForm.get('instrumentName')?.setValue(instrument.instrumentName);
      this.orderForm.get('faceValue')?.setValue(instrument.faceValue);
      this.orderForm.get('price')?.setValidators([Validators.required, priceValidator(instrument.faceValue)])
      this.orderForm.get('quantity')?.setValidators([quantityValidator(instrument.minQuantity)])
      this.orderForm.get('instrumentId')?.setErrors(null)
      this.orderForm.get('price')?.setValue('')
    }, error => {
      this.handleError(error);
      this.orderForm.get('instrumentId')?.setErrors({
        invalid: true
      })
      this.orderForm.get('instrumentName')?.setValue('')
    })
  }

  private handleError(error: any) {
    console.log('Handled Error', error)
    this.dialog.open(ErrorComponent, {
      data: error
    })
  }
}
