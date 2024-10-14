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
}document.getElementById('name').onchange = function () {
    const nameInput = this.value.trim();
    const nameError = document.getElementById('nameError');
    const nameRegex = /^[A-Za-z\s]+$/;

    if (nameInput === '' || !nameRegex.test(nameInput)) {
        nameError.textContent = 'Please enter a valid name (letters only).';
        this.classList.add('invalid');
        this.classList.remove('valid');
    } else {
        nameError.textContent = '';
        this.classList.add('valid');
        this.classList.remove('invalid');
    }
};

document.getElementById('age').onchange = function () {
    const ageInput = this.value.trim();
    const ageError = document.getElementById('ageError');

    if (!/^\d+$/.test(ageInput) || ageInput < 1 || ageInput > 120) {
        ageError.textContent = 'Please enter a valid age (1-120).';
        this.classList.add('invalid');
        this.classList.remove('valid');
    } else {
        ageError.textContent = '';
        this.classList.add('valid');
        this.classList.remove('invalid');
    }
};

document.getElementById('mobile').onchange = function () {
    const mobileInput = this.value.trim();
    const mobileError = document.getElementById('mobileError');
    const mobileRegex = /^\d{10}$/;

    if (!mobileRegex.test(mobileInput)) {
        mobileError.textContent = 'Please enter a valid 10-digit mobile number.';
        this.classList.add('invalid');
        this.classList.remove('valid');
    } else {
        mobileError.textContent = '';
        this.classList.add('valid');
        this.classList.remove('invalid');
    }
};

document.getElementById('gender').onchange = function () {
    const genderInput = this.value.trim();
    const genderError = document.getElementById('genderError');
    const genderRegex = /^(Male|Female|Other)$/i;

    if (!genderRegex.test(genderInput)) {
        genderError.textContent = 'Please enter a valid gender (Male, Female, or Other).';
        this.classList.add('invalid');
        this.classList.remove('valid');
    } else {
        genderError.textContent = '';
        this.classList.add('valid');
        this.classList.remove('invalid');
    }
};

document.getElementById('email').onchange = function () {
    const emailInput = this.value.trim();
    const emailError = document.getElementById('emailError');
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!emailPattern.test(emailInput)) {
        emailError.textContent = 'Please enter a valid email address.';
        this.classList.add('invalid');
        this.classList.remove('valid');
    } else {
        emailError.textContent = '';
        this.classList.add('valid');
        this.classList.remove('invalid');
    }
};

document.getElementById('location').onchange = function () {
    const locationInput = this.value.trim();
    const locationError = document.getElementById('locationError');

    if (locationInput === '') {
        locationError.textContent = 'Location field cannot be empty.';
        this.classList.add('invalid');
        this.classList.remove('valid');
    } else {
        locationError.textContent = '';
        this.classList.add('valid');
        this.classList.remove('invalid');
    }
};
