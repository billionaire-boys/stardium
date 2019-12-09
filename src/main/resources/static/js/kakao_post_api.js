function getAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            let addr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else { // 'J'=지번
                addr = data.jibunAddress;
            }

            let addressComponent = document.getElementById("fullAddress");

            addressComponent.value = addr;
            addressComponent.disabled = true;

            let address = addr.split(" ");

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