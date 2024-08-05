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
    localStorage.removeItem('userToken');
    window.location.href = '/login.html';
}