const inputs = document.querySelectorAll('.form-control input');
const labels = document.querySelectorAll('.form-control label');

labels.forEach(label => {
  label.innerHTML = label.innerText
    .split('')
    .map((letter, idx) => `<span style="
        transition-delay: ${idx * 50}ms
      ">${letter}</span>`)
    .join('');
});

let element=document.getElementById("createAccount")
element.addEventListener("click",createAccount);

//function that directs to registration page
function createAccount(){
    window.location.assign("/registration");
}

//let formElement= document.getElementById("Form");
//formElement.addEventListener("submit",(e)=>{
//    let fullUrl="/process/"+document.getElementsByName("username")[0].value+"/"+document.getElementsByName("password")[0].value;
//    let formElement= document.getElementById("Form");
//    formElement.action=fullUrl;
//    console.log("It has worked fine"+fullUrl);
//});
