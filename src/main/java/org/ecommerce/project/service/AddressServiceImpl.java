package org.ecommerce.project.service;


import org.ecommerce.project.execptions.ResourceNotFoundException;
import org.ecommerce.project.model.Address;
import org.ecommerce.project.model.User;
import org.ecommerce.project.payload.AddressDTO;
import org.ecommerce.project.repository.AddressRepository;
import org.ecommerce.project.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;



    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address=modelMapper.map(addressDTO,Address.class);
        List<Address> addressList=user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedaddress=addressRepository.save(address);
        return modelMapper.map(savedaddress,AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses=addressRepository.findAll();
        List<AddressDTO> addressDTOS= addresses.stream().map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address=addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses=user.getAddresses();
        List<AddressDTO> addressDTOS= addresses.stream().map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
        Address address=addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        address.setPincode(addressDTO.getPincode());
        address.setStreet(addressDTO.getStreet());
        address.setState(addressDTO.getState());
        address.setBuildingName(addressDTO.getBuildingName());
        Address savedAddress=addressRepository.save(address);
        User user=address.getUser();
        user.getAddresses().removeIf(a-> a.getAddressId()==addressId);
        user.getAddresses().add(savedAddress);
        userRepository.save(user);

        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId()==addressId);
        userRepository.save(user);

        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " + addressId;
    }
}
