function confirmResetPassword(event) {
    event.preventDefault(); // Prevent form submission
    Swal.fire({
        title: 'Are you sure to reset to default password (TKE@123)?',
        // text: "This action cannot be undone!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, reset it!'
    }).then((result) => {
        if (result.isConfirmed) {
            event.target.submit(); // Submit the form if confirmed
        }
    });
}
