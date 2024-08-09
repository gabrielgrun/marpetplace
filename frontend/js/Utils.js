class Utils {
    static showAlert(message, alertType = 'success') {
        let alertContainer = document.getElementById('dynamic-alert-container');
        if (!alertContainer) {
            alertContainer = document.createElement('div');
            alertContainer.id = 'dynamic-alert-container';
            alertContainer.style.position = 'fixed';
            alertContainer.style.top = '10px';
            alertContainer.style.right = '10px';
            alertContainer.style.zIndex = '1070';
            document.body.appendChild(alertContainer);
        }

        var alertElement = document.createElement('div');
        alertElement.className = `alert alert-${alertType} alert-dismissible fade show`;
        alertElement.role = 'alert';
        alertElement.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;

        alertContainer.appendChild(alertElement);

        setTimeout(function () {
            var alert = new bootstrap.Alert(alertElement);
            alert.close();
        }, 3000);
    }
}

export default Utils;