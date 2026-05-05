import API from "../../utils/API";
import TokenManager from "../../utils/TokenManager.ts";
import { authGuard } from "../../utils/AuthGuard.ts"
import Router from "../../utils/Router.ts";

const logout = document.querySelector<HTMLFormElement>("#logoutForm");
const transactionForm = document.querySelector<HTMLFormElement>("#transactionForm");
const token = new TokenManager;
const router = new Router;

authGuard();

let api: API;
document.addEventListener("DOMContentLoaded", ()=>{
    api = new API();
    setupCategories(api);
})

logout?.addEventListener("submit", (e)=>{
    e.preventDefault();
    token.clearToken();
    router.go("index");
})

transactionForm?.addEventListener("submit", (e)=>{
    e.preventDefault();

    const formData = new FormData(transactionForm);

    const Transaction = {
        type: formData.get("type"),
        amount: formData.get("amount"),
        category: formData.get("category"),
        date: new Date().getDate(),
        description: formData.get("description"),
        paymentMethod: formData.get("paymentMethod")
    }
    
    api.setUrl("/transaction");

    createTransaction(api, Transaction);

    
})

async function createTransaction(api: API, Transaction: object){
    try{
        const response = await api.fetchPost_withToken(Transaction);
                
        if(response.messages != null){
            response.messages.forEach((message: any) => {
                let singleElement = transactionForm!.elements[message.field] as any;
                singleElement.classList.add("border-danger", "placeholder-color");
                singleElement.placeholder = message.message;
            }); 
            return;
        }
        
        router.go("dashaboard");


    }catch(error){
        console.log(error);
        
    }
}

async function setupCategories(api: API){
    api.setUrl("/categories")
    const categories = await api.fetchGet();
    const categorySelect = document.querySelector<HTMLSelectElement>("#category");

    if(categories.length > 0){
        categorySelect!.innerHTML = ""
        categorySelect!.disabled = false;
        
    }

    categories.forEach((category: any) => {
        categorySelect!.innerHTML +=`
             <option value="${category.category}">${category.category}</option>
        `
        
    });
    
}