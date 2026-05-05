import API from "../../../utils/API";
import TokenManager from "../../../utils/TokenManager";
import Router from "../../../utils/Router";


const form = document.querySelector<HTMLFormElement>("#registerForm");
const token = new TokenManager();
const router = new Router();

if(token.getToken() != null){
    token.clearToken();
}


form?.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = new FormData(form);


    const User = {
        name: formData.get("name") as string,
        surname: formData.get("surname") as string,
        email: formData.get("email") as string,
        password: formData.get("password") as string,
    }
    
    const api = setupApi();

    registerUser(api, User);
    
})

function setupApi(): API{
    const api = new API();
    api.setUrl("/auth/register")
    return api;
}

async function registerUser(api: API, User: object){
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
        
        router.go("login");
        
    }catch(error){
        console.error(error);
    }
}