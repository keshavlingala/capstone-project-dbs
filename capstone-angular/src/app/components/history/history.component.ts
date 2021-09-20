import {Component, OnInit} from '@angular/core';
import {OrderBook} from "../../models/models";
import {MatTableDataSource} from "@angular/material/table";
import {DataService} from "../../services/data.service";
import {ThemePalette} from "@angular/material/core";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent implements OnInit {
  orderBookColumns = ['clientId', 'custodian', 'instrumentId', 'price', 'orderStatus', 'timestamp', 'orderDirection','icon']
  orderBook!: MatTableDataSource<OrderBook>

  constructor(
    private data: DataService
  ) {
    this.data.getAllOrderBook().subscribe(orderBook => {
      this.orderBook = new MatTableDataSource<OrderBook>(orderBook);
    })
  }

  ngOnInit(): void {

  }

  asOrder(element: any) {
    return element as OrderBook;
  }

  getColor(element: any): ThemePalette {
    if (this.asOrder(element).orderStatus == 'PROCESSING') return 'accent'
    if (this.asOrder(element).orderStatus == 'COMPLETED') return 'primary'
    return 'warn'
  }

  getIcon(element:any) {
    if(this.asOrder(element).orderStatus=='COMPLETED')return 'done'
    if(this.asOrder(element).orderStatus=='PROCESSING')return 'pending_actions'
    return 'cancel'
  }
}
