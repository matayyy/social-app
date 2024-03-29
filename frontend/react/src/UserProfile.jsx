const UserProfile = ({name, age, gender, number, ...props}) => {

    gender = gender === "MALE" ? "men" : "lego"

    return (
        <div>
            <h1>{name}</h1>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/${gender}/${number}.jpg`}/>
            {props.children}
        </div>
    )
}

export default UserProfile;