export interface Bus {
    operator: string
    serviceNo: string
    busStopDetails: string
    // ? because it is a promise, ? means if it exists then use the values else treat it as null
    nextBus1?: BusDetails
    nextBus2?: BusDetails
    nextBus3?: BusDetails
}

export interface BusDetails {
    eta: string
    latitude: number
    longtitude: number
    load: string
    feature: string
    type: string
}

export interface TrainDetails {
    trainDetails?: Train[]
    trainStart?: string
    trainEnd?: string
    trainLineName?: string
}

export interface Train {
    stationName: string
    crowdLevel: string
}

export interface Weathers {
    updatedTimeStamp: string
    validEndTime: string
    validStartTime: string
    listOfWeatherInAreas: WeatherArea[]
}

export interface WeatherArea {
    area: string
    forecast: string
}

export interface Register {
    firstName: string
    lastName: string
    email: string
    password: string
    repeatpw: string
}

export interface TrafficCameras {
    odata_metadata: string
    value: TrafficCamera[]
}

export interface TrafficCamera {
    CameraID: string
    Latitude: string
    Longtitude: string
    ImageLink: string
}

export interface FileUpload {
    fileUpload: File | Blob
    title: string
    description: string
}

export interface TrafficImages {
    email: string
    firstName: string
    url: string
    title: string
    description: string
    date: string
}

export interface LoginDetails {
    email: string
    password: string
}

export interface UserAuthenticated {
    email: string
    authentication: boolean
    firstName: string
    lastName: string
    favLocation: string
}

export interface CanLeave {
    stringInWhichTs: string
    canLeave(): boolean
}
