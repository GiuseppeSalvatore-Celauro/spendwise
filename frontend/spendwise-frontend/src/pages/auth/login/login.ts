import API from "../../../utils/API";

const form = document.querySelector<HTMLFormElement>("#loginForm");

if(localStorage.getItem("token") != null){
    localStorage.clear();
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
        const response = await api.fetchPost_forText(User);
        localStorage.setItem("token", response);

        if(localStorage.getItem("token")){
            window.location.href = "../../dashboard/dashboard.html";
        }
        
    }catch(error){
        console.error(error);
    }
}