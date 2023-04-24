import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import Dexie from "dexie";
import { Observable } from "rxjs";
import { CanLeave, UserAuthenticated } from "./models";

@Injectable({ providedIn: 'root' })
export class UserRepository extends Dexie {

    // Not used
    userDetailsDexie: Dexie.Table<UserAuthenticated, string>

    // Not used
    constructor(private router: Router) {
        // Name of the database
        super('userDetailsDB')

        // Note we must use a different version when we are storing diff or more data
        this.version(1).stores({
            // This means to store at userDetailsTable table, with email as the Primary Key
            // Which means email must be unique
            // The email auto comes from the userAuth paramter in addUserDetails
            userDetailsTable: 'email'
        })

        // This is needed for retrieval of data
        // this.table is from the dexie library that we imported
        this.userDetailsDexie = this.table('userDetailsTable')
    }

    addUserDetails(userAuth: UserAuthenticated): Promise<string> {
        console.info('in adduserdetails', userAuth)
        // usually .add(value, key) is like this but when we declare userDetailsTable: 'email', it wil automatically take email value from userAuth
        return this.userDetailsDexie.add(userAuth)
    }

    //The parameters in here are standard
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot)
        : boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        
        console.info('>>> In user.repository.ts, canActivate()')
        
        if (sessionStorage.length < 1) {
            return this.router.parseUrl('/')
        }

        const user = JSON.parse(sessionStorage.getItem('user')!)

        if (user['authentication'] == true) {
            console.info('authentication == true ')
            return true
        }

        return this.router.parseUrl('/')
    }

    // Not used
    // The parameters in here are standard
    canDeactivate(component: CanLeave, currentRoute: ActivatedRouteSnapshot, currentState: RouterStateSnapshot, nextState: RouterStateSnapshot)
        : boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {

        console.info('>>> In', component, ', canDeactivate()')
        console.info(component.stringInWhichTs)

        // CanLeave Model in model.ts
        if (component.canLeave()) {
            console.info('canLeave() is true')
            return true
        }

        // true -> OK, false -> Cancel
        // return prompt('Type YES to exit') == "YES"
        //return confirm('You have not save your data. Leave?')
        return false;
    }

}