document.addEventListener("DOMContentLoaded", init);

function init() {
    maskInput();
}

function maskInput() {
    const inputContato = document.getElementById("inputContato");

    inputContato.addEventListener("input", function () {
        const phone = phoneMask(this.value);
        this.value = phone;
    });

    inputContato.addEventListener("input", function (e) {
        var x = e.target.value.replace(/\D/g, "").match(/(\d{0,2})(\d{0,5})(\d{0,4})/);
        e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
    });
}

function phoneMask(phone) {
    const mask = "(##) #####-####";
    let str = "";

    for (let i = 0, j = 0; i < phone.length; i++) {
        if (mask.charAt(i) === "#" && phone.charAt(i) !== "") {
            str += phone.charAt(i);
            j++;
        } else if (mask.charAt(i) !== "#" && j < mask.length) {
            str += phone.charAt(i);
            j++;
        }
    }

    return str;
}