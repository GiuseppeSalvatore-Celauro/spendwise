import API from "../../../utils/API";
import TokenManager from "../../../utils/TokenManager";
import Router from "../../../utils/Router";

const form = document.querySelector<HTMLFormElement>("#loginForm");
const token = new TokenManager();
const router = new Router();

if(token.getToken() != null){
    token.clearToken();
}

form?.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = new FormData(form);


    const User = {
        email: formData.get("email") as string,
        password: formData.get("password") as string,
    }
    
    const api = setupApi();

    loginUser(api, User);
    
})

function setupApi(): API{
    const api = new API();
    api.setUrl("/auth/login")
    return api;
}

async function loginUser(api: API, User: object){
    try{
        const response = await api.fetchPost(User);
        console.log(response);
        token.setToken(response);

        if(token.getToken()){
            router.go("dashaboard");
        }
        
    }catch(error){
        console.error(error);
    }
}