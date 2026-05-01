const logout = document.querySelector<HTMLFormElement>("#logoutForm");

if(localStorage.getItem("token") == null ){
    window.location.href = "../auth/login/login.html"
}

logout?.addEventListener("submit", (e)=>{
    e.preventDefault();
    localStorage.clear();
    window.location.href = "../../index.html"
})