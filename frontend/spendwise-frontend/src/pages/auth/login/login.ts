import API from "../../../utils/API";
import TokenManager from "../../../utils/TokenManager";
import Router from "../../../utils/Router";

const form = document.querySelector<HTMLFormElement>("#loginForm");
const allertBox = document.querySelector("#allertBox");
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

        if(response.messages != null){
            response.messages.forEach((message: any) => {
                let singleElement = form!.elements[message.field] as any;
                singleElement.classList.add("border-danger", "placeholder-color");
                singleElement.placeholder = message.message;
            }); 
            return;
        }

        if(response.message != null){
            allertBox?.classList.remove("my-5")
            allertBox?.classList.add("mt-5")

            allertBox!.innerHTML = `
                <div class="alert alert-danger id="loginAllert" role="alert">
                    invalid credentials
                </div>
            `

            return;
        }

        token.setToken(response);
        router.go("dashaboard");
        
    }catch(error){
        console.error(error);
    }
}