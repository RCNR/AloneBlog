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


// 생성
const modifyButton = document.getElementById("create-btn");

if (modifyButton) {

    modifyButton.addEventListener("click", (event) => {
        fetch("/api/articles", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById("title").value,
                content: document.getElementById("content").value,
            })
        }).then(() => {
            alert("글 등록이 완료되었습니다.");
            location.replace("/articles")
        });
    })
}

// 수정
const modifyButton = document.getElementById("modify-btn");

if (modifyButton) {

    modifyButton.addEventListener("click", event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`api/articles/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById("title").value,
                content: document.getElementById("content").value,
            })
        }).then(() => {
            alert("글 수정이 완료되었습니다.");
            location.replace(`articles/${id}`)
        });
    })
}