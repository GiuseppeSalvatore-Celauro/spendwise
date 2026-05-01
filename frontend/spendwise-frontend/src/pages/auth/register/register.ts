import API from "../../../utils/API";

const form = document.querySelector<HTMLFormElement>("#registerForm");

if(localStorage.getItem("token") != null){
    localStorage.clear();
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
        await api.fetchPost(User);
        window.location.href = "../login/login.html";
        
    }catch(error){
        console.error(error);
    }
}