export default class Router{
    private routes = {
        index: "/index.html",
        login: "/src/pages/auth/login/login.html",
        register: "/src/pages/auth/register/register.html",
        dashaboard: "/src/pages/dashboard/dashboard.html",
        newCategory: "/src/pages/new_category/new_category.html",
        newTransaction: "/src/pages/new_transaciton/new_transaciton.html"
    }

    go(route: keyof typeof this.routes){
        window.location.href = this.routes[route];
    }
    
}