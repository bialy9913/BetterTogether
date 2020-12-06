package com.example.bettertogether.ui.settings

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.models.ValidatePassword
import com.example.bettertogether.repositories.AuthRepository
import com.example.bettertogether.repositories.UserRepository
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.info
import com.example.bettertogether.utils.validatePassword
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) :BaseViewModel<SettingsNavigator>(){
    var validateOldPassword = ValidatePassword("",false)
    var validateNewPassword = ValidatePassword("",false)
    var seekBarValue = ObservableField("")
    var lastMaxDistance:String=""
    var newStartPoint = ObservableField<String>()
    var newStartPointLat: Double=0.0
    var newStartPointLng: Double=0.0
    private var lastStartPoint=""


    fun setSeekBarValueChange(progress:Int){
        seekBarValue.set(progress.toString())
        seekBarValue.notifyChange()
    }

    fun oldPasswordOnTextChanged(s:CharSequence){
        if(s.isEmpty()){
            validateOldPassword.isValid=false
            validateOldPassword.setError(null)
        }
        else{
            if(validatePassword(s.toString())){
                validateOldPassword.isValid=true
                validateOldPassword.setError(null)
            }
            else{
                validateOldPassword.isValid=false
                validateOldPassword.setError("Password must have at least 6 characters")
            }
        }
    }

    fun newPasswordOnTextChanged(s:CharSequence){
        if(s.isEmpty()){
            validateNewPassword.isValid=false
            validateNewPassword.setError(null)
        }
        else{
            if(validatePassword(s.toString())){
                validateNewPassword.isValid=true
                validateNewPassword.setError(null)
            }
            else{
                validateNewPassword.isValid=false
                validateNewPassword.setError("Password must have at least 6 characters")
            }
        }
    }

    fun changeMaxDistance(){
        viewModelScope.launch {
            if(lastMaxDistance==seekBarValue.get().toString()){
                navigator()?.onChangeMaxDistanceFailure("Nothing to change")
            }
            if(lastMaxDistance!=seekBarValue.get().toString()){
                userRepository.changeMaxDistance(
                    seekBarValue.get().toString(),
                    onStarted = {navigator()?.onChangeMaxDistanceStarted()},
                    onSuccess = {
                        lastMaxDistance=seekBarValue.get().toString()
                        navigator()?.onChangeMaxDistanceSuccess()
                    },
                    onFailure = {message -> navigator()?.onChangeMaxDistanceFailure(message.toString())}
                )
            }
        }
    }

    fun changePassword(){
        if(!(validateOldPassword.isValid&&validateNewPassword.isValid))
            return
        if(validateOldPassword.password!=validateNewPassword.password){
            navigator()?.onChangePasswordFailure("Passwords are not equal")
            return
        }

        viewModelScope.launch {
            authRepository.changePassword(
                validateOldPassword.password
                ,validateNewPassword.password
                ,onStarted = {
                navigator()?.onChangePasswordStarted()
                },onSuccess = {
                    navigator()?.onChangePasswordSuccess()
                },onFailure = {message ->
                    navigator()?.onChangePasswordFailure(message.toString())
                })
        }
    }

    fun getMaxDistance(){
        viewModelScope.launch {
            userRepository.getMaxDistance(
                onStarted = {navigator()?.onGetDefaultSettingsStarted()},
                onSuccess = {
                    lastMaxDistance=it
                    getStartPoint()
                },
                onFailure = {message -> navigator()?.onGetDefaultSettingsFailure(message)}
            )
        }
    }
    private fun getStartPoint(){
        var defaultStartPoint:List<String> = listOf()
        viewModelScope.launch {
            userRepository.getDefaultStartPoint(
                onStarted = {},
                onSuccess = {defaultStartPointParam ->
                    defaultStartPoint=defaultStartPointParam.split(";")
                    lastStartPoint = if(!defaultStartPoint.isNullOrEmpty() && !defaultStartPoint[0].isNullOrEmpty()){
                        defaultStartPoint[0]
                    } else{
                        "No default start point set"
                    }
                    newStartPoint.set(lastStartPoint)
                    newStartPoint.notifyChange()
                    navigator()?.onGetDefaultSettingsSuccess(lastMaxDistance)
                },
                onFailure = {message ->
                    navigator()?.onGetDefaultSettingsFailure(message)}
            )
            info("lastStartPoint: $lastStartPoint")
        }
    }
    fun onDefaultStartPointClick(){
        navigator()?.onDefaultStartPointClick()
    }
    fun changeDefaultStartPoint(){
        if(lastStartPoint==newStartPoint.get()){
            navigator()?.onChangeDefaultStartPointFailure("Nothing to change")
            return
        }
        viewModelScope.launch {
            userRepository.changeDefaultStartPoint(
                newStartPoint.get()!!,
                newStartPointLat,
                newStartPointLng,
                onStarted = {navigator()?.onChangeDefaultStartPointStarted()},
                onSuccess = {
                    lastStartPoint=newStartPoint.get()!!
                    navigator()?.onChangeDefaultStartPointSuccess()
                },
                onFailure = {message ->
                    navigator()?.onChangeDefaultStartPointFailure(message.toString())
                }
            )
        }
    }
}