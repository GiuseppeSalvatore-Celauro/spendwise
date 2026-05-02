export default class API{
    url: string = "";
    
    getUrl(): string{
        return this.url;
    }

    setUrl(url: string): void{
        this.url = `http://localhost:8080/api${url}`;
    }

    async fetchPost(data: object){
        const rawResponse = await fetch(this.url, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-type" : "application/json"
            },
            body: JSON.stringify(data)
        });
        const content = await rawResponse.json();
        
        return content;
    }

    async fetchPost_forText(data: object){
         const rawResponse = await fetch(this.url, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-type" : "application/json"
            },
            body: JSON.stringify(data)
        });
        const content = await rawResponse.text();
        
        return content;
    }

    async fetchPut(data: object){
        const rawResponse = await fetch(this.url, {
            method: "PUT",
            headers: {
                "Accept": "application/json",
                "Content-type" : "application/json"
            },
            body: JSON.stringify(data)
        });
        const content = await rawResponse.json();
        return content;
    }

    async fetchGet(){
        const token = localStorage.getItem("token")
        if(!token){
            return null;
        }

        const rawResponse = await fetch (this.url, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        })
        const content = await rawResponse.json();
        return content;  
    }
}