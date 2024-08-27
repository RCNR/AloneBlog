// 삭제
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {

        const confirmDeletion = confirm("정말 삭제하시겠습니까?");

        if (confirmDeletion) {
            let id = document.getElementById('article-delete-id').value;
            fetch(`/api/articles/${id}`, { // BlogApiController의 DeleteMapping이 실행된다.
                method: 'DELETE'
            })
                .then(() => {
                    alert("삭제가 완료 되었습니다.");
                    location.replace('/articles');
                })
        } 
    });
}