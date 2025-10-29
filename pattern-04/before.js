// 변경 전: get/fetch/retrieve를 뒤섞어 사용 -> 팀 내 혼란
function getUser(id){ /* ... */ return {id, name:'Kim'} }
function fetchUserInfo(id){ /* ... */ return {id, email:'a@b.c'} }
function retrieveUserProfile(id){ /* ... */ return {id, age: 20} }
