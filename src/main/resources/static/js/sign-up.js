const CHECK_SIGN_UP = (() => {

    const SignUpController = function () {
        const signUpService = new SignUpService();

        const checkValid = () => {
            document.querySelector("#submit-btn").addEventListener("click", signUpService.checkValid)
        };

        const init = () => {
            checkValid();
        };

        return {
            init
        }
    };

    const SignUpService = function () {
        const signUpModel = new SignUpModel();

        const checkValid = (event) => {
            const email = document.querySelector("#email");
            const password = document.querySelector("#password");
            const confirmPassword = document.querySelector("#password-confirm");
            const nickname = document.querySelector("#nickname");

            if(event.target.tagName === "BUTTON") {
                const message = signUpModel.checkValue(email, password, confirmPassword, nickname);
                if(message !== "") {
                    alert(message);
                    return;
                }
                document.querySelector("#sign-up-form").submit();
            }
        };

        return {
            checkValid
        }
    };

    const SignUpModel = function () {

        const checkValue = (email, password, confirmPassword, nickname) => {
            if (email.value === "") {
                return "이메일 입력해주세요";
            }

            if (password.value === "") {
                return "비밀번호 입력 해주세요.";
            }

            if (confirmPassword.value === "") {
                return "확인비밀번호를 입력 해주세요.";
            }

            if (nickname.value === "") {
                return "닉네임을 입력해주세요";
            }

            if (password.value !== confirmPassword.value) {
                return "비밀번호와 확인 비밀번호가 일치하지 않습니다.";
            }

            return "";
        };

        return {
            checkValue
        }

    };

    const init = () => {
        const signController = new SignUpController();
        signController.init();
    };

    return {
        init
    }
})();

CHECK_SIGN_UP.init();