import API from "../../utils/API";
import TokenManager from "../../utils/TokenManager.ts";
import { authGuard } from "../../utils/AuthGuard.ts"
import Router from "../../utils/Router.ts";

const logout = document.querySelector<HTMLFormElement>("#logoutForm");
const categoryForm = document.querySelector<HTMLFormElement>("#categoryForm");
const token = new TokenManager;
const router = new Router;

authGuard();

logout?.addEventListener("submit", (e)=>{
    e.preventDefault();
    token.clearToken();
    router.go("index");
})

categoryForm?.addEventListener("submit", (e)=>{
    e.preventDefault();

    const formData = new FormData(categoryForm);

    const Category = {
        category: formData.get("category"),
    }
    
    const api = setupApi();

    createTransaction(api, Category);
    
})

function setupApi(): API{
    const api = new API();
    api.setUrl("/category")
    return api;
}

async function createTransaction(api: API, Category: object){
    try{
        const response = await api.fetchPost_withToken(Category);

        if(response.messages != null){
            response.messages.forEach((message: any) => {
                let singleElement = categoryForm!.elements[message.field] as any;
                singleElement.classList.add("border-danger", "placeholder-color");
                singleElement.placeholder = message.message;
            }); 
            return;
        }
        
        router.go("newTransaction");
        
    }catch(error){
        console.error(error);
    }
}