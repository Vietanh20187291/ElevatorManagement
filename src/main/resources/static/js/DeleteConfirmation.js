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

function confirmDeleteBuilding(event) {
    event.preventDefault(); // Prevent form submission
    Swal.fire({
        title: 'Important!',
        text: "This action will delete all resources of this building and disable all users related to the building.\nType 'Yes' to confirm.",
        icon: 'warning',
        input: 'text', // Add text input
        inputPlaceholder: 'Type "Yes" to confirm',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Delete'
    }).then((result) => {
        if (result.isConfirmed && result.value === 'Yes') {
            event.target.submit(); // Submit the form if confirmed and input is "Yes"
        } else if (result.isConfirmed && result.value !== 'Yes') {
            Swal.fire({
                title: 'Action not confirmed',
                text: 'You must type "Yes" to proceed.',
                icon: 'error'
            });
        }
    });
}


