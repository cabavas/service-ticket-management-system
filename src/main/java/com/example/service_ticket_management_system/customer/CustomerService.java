package com.example.service_ticket_management_system.customer;

import com.example.service_ticket_management_system.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<CustomerResponseDTO> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    public CustomerResponseDTO findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return toResponseDTO(customer);
    }

    public CustomerResponseDTO create(CustomerRequestDTO requestDTO) {
        Customer customer = toEntity(requestDTO);
        customer.setId(null);
        Customer saved = customerRepository.save(customer);
        return toResponseDTO(saved);
    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO requestDTO) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        existing.setEmail(requestDTO.email());
        existing.setName(requestDTO.name());
        existing.setPhone(requestDTO.phone());

        Customer updated = customerRepository.save(existing);
        return toResponseDTO(updated);
    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        customerRepository.delete(customer);
    }

    private Customer toEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setPhone(dto.phone());
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        return customer;
    }

    private CustomerResponseDTO toResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }
}
