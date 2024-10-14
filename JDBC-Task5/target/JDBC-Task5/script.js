const closeButton = document.querySelector(".close-button");
const addUserModal = document.querySelector(".add-customer-modal");
const addButton = document.querySelector(".add-customer");


closeButton.onclick = function(){

    addUserModal.style.display = "none";
}

addButton.onclick = function(){
    addUserModal.style.display = "flex";
}
document.getElementById('add-customer-button').addEventListener('click', () => {
    openAddModal();
});

function openAddModal() {
    document.getElementById('customerForm').action = 'AddServlet';
    document.getElementById('modal-title').innerText = 'ADD CUSTOMERS';
    document.getElementById('form-submit').value = 'Submit';
    document.getElementById('customerForm').reset();
    document.getElementById('customerId').value = '';
    document.getElementById('addUserModal').style.display = 'flex';
}

document.querySelector('.close-button').addEventListener('click', () => {
    document.getElementById('addUserModal').style.display = 'none';
});



function validateFields() {
   
    let name = document.getElementById("name").value.trim();
    let age = document.getElementById("age").value.trim();
    let mobile = document.getElementById("mobile").value.trim();
    let gender = document.getElementById("gender").value.trim();
    let email = document.getElementById("email").value.trim();
    let location = document.getElementById("location").value.trim();

    let nameregex = /^[a-zA-Z\s]+$/;
    let mobilenumberregex = /^\d{10}$/;
    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    let genderregex = /^(Male|Female|Other)$/i;

    

    if (!nameregex.test(name)) {
        displayErrorMessage("Please enter a valid name!");
        return false;  
    }

    if (!/^\d+$/.test(age) || age < 1 || age > 100) {
        displayErrorMessage("Please enter a valid age between 1 and 100!");
        return false;  
    }

    if (!mobilenumberregex.test(mobile)) {
        displayErrorMessage("Please enter a valid 10-digit mobile number!");
        return false; 
    }

    if (!genderregex.test(gender)) {
        displayErrorMessage("Please enter a valid gender (Male, Female, or Other)!");
        return false; 
    }

    if (!emailPattern.test(email)) {
        displayErrorMessage("Please enter a valid email address!");
        return false;  
    }

    if (location === "") {
        displayErrorMessage("Location field cannot be empty!");
        return false;  
    }
    return true;  
}

function displayErrorMessage(message) {
    const errormsgDiv = document.querySelector(".error-message");

    errormsgDiv.textContent = message; 
    errormsgDiv.style.visibility = 'visible';
    errormsgDiv.style.color = "red";

    setTimeout(() => {
        errormsgDiv.style.visibility = 'hidden';
    }, 2500);
}
