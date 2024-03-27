function confirmDelete(event) {
    event.preventDefault(); // Prevent form submission
    Swal.fire({
        title: 'Are you sure to delete?',
        // text: "This action cannot be undone!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            event.target.submit(); // Submit the form if confirmed
        }
    });
}