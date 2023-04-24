import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http"
import { Subject, first, firstValueFrom } from "rxjs";
import { FileUpload, LoginDetails, Register } from "./models";

@Injectable()
export class HttpServices {

    // For navbar
    refresh = new Subject<boolean>()

    // To store imageData in trafficupdates
    imageData = ""

    constructor(private http: HttpClient) { }
    
    getBus(busCode: string): Promise<any> {
        const params = new HttpParams()
            .set("busCode", busCode)
            
        return firstValueFrom(
            this.http.get<any>('/api/getBusTiming', { params })
        )
    }

    getWeather(): Promise<any> {
        return firstValueFrom(
            this.http.get<any>('/api/weatherPage')
        )
    }

    setFavWeather(area: string, email: string): Promise<any> {
        const headers = new HttpHeaders()
                .set('Content-Type', 'application/x-www-form-urlencoded')
        
        let qs = new HttpParams()
            .set("favourite", area)
            .set("email", email)

        return firstValueFrom(
            this.http.put<any>('/api/favArea', qs.toString() ,{ headers })
        )
    }

    getFavWeather(email: string): Promise<any> {
        let qs = new HttpParams()
            .set("email", email)

        return firstValueFrom(
            this.http.get<any>('/api/getFavArea/', { params: qs })
        )
    }

    getTrainCrowd(trainCode: string): Promise<any> {
        return firstValueFrom(
            this.http.get<any>(`/api/getTrainCrowd/${trainCode}`)
        )
    }

    register(registerData: Register) {
        const headers = new HttpHeaders()
                .set('Content-Type', 'application/x-www-form-urlencoded')

        let qs = new HttpParams()
            .set("firstName", registerData.firstName)
            .set("lastName", registerData.lastName)
            .set("email", registerData.email)
            .set("password", registerData.password)
            .set("repeatpw", registerData.repeatpw)
        
        return firstValueFrom(
            this.http.post<any>('/api/register', qs.toString(), { headers })
        )
    }

    getTrafficImages(): Promise<any> {
        return firstValueFrom(
            this.http.get('/api/getTrafficImages')
        )
    }

    uploadFile(form: FileUpload, email: string): Promise<any> {
        console.info(form)

        const formData = new FormData()
        formData.append('file', form.fileUpload)
        formData.append('title', form.title)
        formData.append('description', form.description)
        formData.append('email', email)

        //console.info(formData.get('email'))

        return firstValueFrom(
            this.http.post<any>('/api/uploadFile', formData )
        )
    }

    getLocationService(): Promise<any> {
        return new Promise((resolve, reject) => {
            navigator.geolocation.getCurrentPosition(resp=> {
                resolve({lng: resp.coords.longitude, lat: resp.coords.latitude})
                // reject("Wrong")
            })
        })
    }

    getUsersTrafficImages(): Promise<any> {
        return firstValueFrom(
            this.http.get<any>('/api/getTrafficUpdates')
        )
    }

    userLogin(logindetails: LoginDetails): Promise<any> {

        const params = new HttpParams()
            .set("email", logindetails.email)
            .set("password", logindetails.password)

        return firstValueFrom(
            this.http.get<any>('/api/login', { params })
        )
    }

    postKey(email: string, token: string) {
        const headers = new HttpHeaders()
            .set('Content-Type', 'application/x-www-form-urlencoded')

        let qs = new HttpParams()
            .set('email', email)
            .set('token', token)

        return firstValueFrom(
            this.http.post<any>('/api/postKey', qs.toString(), { headers } )
        )
    }

}