// 삭제
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, { // BlogApiController의 DeleteMapping이 실행된다.
            method: 'DELETE'
        })
            .then(() => {
                alert("삭제 완료 되었습니다.");
                location.replace('/articles');
            })
    });
}