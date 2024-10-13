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



