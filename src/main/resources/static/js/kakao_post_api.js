function getAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            let addr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else { // 'J'=지번
                addr = data.jibunAddress;
            }

            let address = addr.split(" ");

            if(address[0] !== "서울") {
                alert("서울시만 가능합니다.");
                return;
            }

            document.getElementById("fullAddress").value = addr;
            document.getElementById("fullAddress").disabled = true;
            document.getElementById("city").value = address[0];
            document.getElementById("section").value = address[1];

            let detailAddress = '';

            for (let i = 2; i < address.length; i++) {
                detailAddress += address[i] + " ";
            }

            document.getElementById("detail").value = detailAddress;
        }
    }).open();
}