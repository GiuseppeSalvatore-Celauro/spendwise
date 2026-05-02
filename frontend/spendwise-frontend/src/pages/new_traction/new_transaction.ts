import API from "../../utils/API";

const logout = document.querySelector<HTMLFormElement>("#logoutForm");
const transactionForm = document.querySelector<HTMLFormElement>("#transactionForm");

let api: API;
document.addEventListener("DOMContentLoaded", ()=>{
    api = new API();
    setupCategories(api);
})

logout?.addEventListener("submit", (e)=>{
    e.preventDefault();
    localStorage.clear();
    window.location.href = "../../../index.html"
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
        console.log(Transaction);
        
        if(response){
            window.location.href = "../dashboard/dashboard.html";
        }
        
    }catch(error){
        console.error(error);
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