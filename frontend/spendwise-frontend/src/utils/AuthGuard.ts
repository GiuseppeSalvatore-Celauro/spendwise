import TokenManager from "./TokenManager";
import Router from "./Router";

const token = new TokenManager();
const router = new Router;

export function authGuard(){
    if(token.getToken() == null){
        router.go("login");
    }
}