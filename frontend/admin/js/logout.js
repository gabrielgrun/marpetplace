document.addEventListener("DOMContentLoaded", init);

function init() {
    bindBtnLogout();
}

function bindBtnLogout(){
    document.querySelector('.logout').addEventListener('click', logout);
}

function logout(e){
    e.preventDefault();
    e.stopPropagation();
    localStorage.removeItem('adminToken');
    window.location.href = '/admin/login.html';
}