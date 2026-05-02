import API from "../../utils/API";

const logout = document.querySelector<HTMLFormElement>("#logoutForm");
const categoryForm = document.querySelector<HTMLFormElement>("#categoryForm");

logout?.addEventListener("submit", (e)=>{
    e.preventDefault();
    localStorage.clear();
    window.location.href = "../../../index.html"
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
        
        if(response){
            window.location.href = "../dashboard/dashboard.html";
        }
        
    }catch(error){
        console.error(error);
    }
}