import { Component } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';

import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-donate',
  templateUrl: './donate.component.html',
  styleUrls: ['./donate.component.css']
})
export class DonateComponent {

  url: string = 'https://everydaysg.up.railway.app'
  local: string = 'http://localhost:4200'

  // We load  Stripe
  stripePromise = loadStripe(environment.stripe);
  constructor(private http: HttpClient) {}

  async pay(): Promise<void> {
    // here we create a payment object
    const payment = {
      name: 'Donate',
      currency: 'usd',
      // amount on cents *10 => to be on dollar
      amount: 500,
      quantity: '1',
      cancelUrl: `${this.url}/#/home/donate`,
      successUrl: `${this.url}/#/home/thankyou`,
    };

    const stripe = await this.stripePromise;

    // Can put in service
    // this is a normal http calls for a backend api
    this.http
      .post(`${environment.serverUrl}/payment`, payment)
      .subscribe((data: any) => {
        // I use stripe to redirect To Checkout page of Stripe platform
        stripe?.redirectToCheckout({
          sessionId: data.id,
        });
      });
  }
}
