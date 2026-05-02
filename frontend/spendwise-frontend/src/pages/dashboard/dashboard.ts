import API from "../../utils/API";

const logout = document.querySelector<HTMLFormElement>("#logoutForm");
const incomeValue = document.querySelector<HTMLParagraphElement>("#totalIncome");
const expenseValue = document.querySelector<HTMLParagraphElement>("#totalExcpenses");
const accordionFlushExample = document.querySelector<HTMLDivElement>("#accordionFlushExample");

let totalExpenses = 0;
let totalIncome = 0;

document.addEventListener("DOMContentLoaded", () => {
    if(localStorage.getItem("token") == null ){
        window.location.href = "../auth/login/login.html"
    }

    const api = new API();
    
    setupTotalValues(api);
    setupTransactionAccordion(api);
})


logout?.addEventListener("submit", (e)=>{
    e.preventDefault();
    localStorage.clear();
    window.location.href = "../../../index.html"
})

async function loadMontlyInocomeAndEpenses(api: API){
    const transactions = await api.fetchGet()
    totalExpenses += transactions.totalExpense;
    totalIncome += transactions.totalIncome;
}

async function setupTotalValues(api:API){
    const date = new Date();
    api.setUrl(`/transactions/month?month=${date.getMonth()+1}&year=${date.getFullYear()}`);
    await loadMontlyInocomeAndEpenses(api);

    incomeValue!.innerHTML = totalIncome.toString();
    expenseValue!.innerHTML = totalExpenses.toString();
}

async function loadAllTransactions(api:API){
    const transactions = await api.fetchGet();
    return transactions;
}

async function setupTransactionAccordion(api:API){
    api.setUrl("/transactions");
    const transactions = await loadAllTransactions(api);

    if(transactions.length == 0){
        const transactionsTitle = document.querySelector("#transactionsTitle");
        transactionsTitle!.innerHTML = "Non hai ancora registrato nessuna transazione";
        transactionsTitle?.classList.add("text-center")
        accordionFlushExample?.classList.add("hidden")
    }

    transactions.forEach((transaction:any, i:number) => {
        let tipo;
        if(transaction.type == "EXPENSE") tipo = "Spesa";
        if(transaction.type == "INCOME") tipo = "Incasso";
        accordionFlushExample!.innerHTML += `
            <div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse${i}" aria-expanded="false" aria-controls="flush-collapseOne">
                        ${tipo}: ${transaction.category}
                    </button>
                </h2>
                <div id="flush-collapse${i}" class="accordion-collapse collapse" data-bs-parent="#accordionFlushExample">
                    <div class="accordion-body">
                        <div>
                            <p class="h2">
                                Totale: ${transaction.amount}
                            </p>
                            <p>
                                ${transaction.description}
                            </p>
                            <p class="fs-custom-10 fw-secondary">
                                Metodo di pagamento: ${transaction.paymentMethod}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        `
    });
    

}