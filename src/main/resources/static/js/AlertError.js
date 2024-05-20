function alertError(errorMessage) {
    Swal.fire({
        title: 'Error',
        text: errorMessage,
        icon: 'error'
    });
}
function alertSuccess(successMessage) {
    Swal.fire({
        title: 'Success',
        text: successMessage,
        icon: 'success'
    });
}
