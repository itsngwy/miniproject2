import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from "@angular/router";
import { LoginComponent } from "./loginregister/login.component";
import { CanLeave } from "./models";
import { Observable } from "rxjs";

export const exitAboutGuard=(component: CanLeave, currentRoute: ActivatedRouteSnapshot, currentState: RouterStateSnapshot, nextState: RouterStateSnapshot)
      : boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree>  => {

  if(component.canLeave()){
      return confirm("Вы хотите покинуть страницу?");
  }

  return true;
}