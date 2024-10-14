document.getElementById('name').onchange = function () {
    const nameInput = this.value;
    const nameError = document.getElementById('nameError');
    
    // Name validation: must not be empty and should contain only letters
    if (nameInput.trim() === '' || !/^[A-Za-z]+$/.test(nameInput)) {
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
    const ageInput = this.value;
    const ageError = document.getElementById('ageError');
    
    // Age validation: must be a number between 1 and 120
    if (ageInput < 1 || ageInput > 120) {
        ageError.textContent = 'Please enter a valid age (1-120).';
        this.classList.add('invalid');
        this.classList.remove('valid');
    } else {
        ageError.textContent = '';
        this.classList.add('valid');
        this.classList.remove('invalid');
    }
};
