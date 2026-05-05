export default class TokenManager{

    public setToken(response: any){
        localStorage.setItem("token", response.token);
        localStorage.setItem("expiration", response.expDate);
    }

    public getToken(): string{
        return localStorage.getItem("token") as string;
    }

    public getExpiration(){
        return localStorage.getItem("expiration");
    }

    public clearToken(){
        localStorage.clear();
    }
}